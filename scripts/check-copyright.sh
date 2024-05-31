#!/bin/sh

# Copyright 2024 IBM Corporation and others.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an \"AS IS\" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# SPDX-License-Identifier: Apache-2.0

# Stop on first unexpected error
set -e

die() {
  echo "$@" >&2
  exit 1
}

# Check that git is a command
command -v git > /dev/null 2>&1 || die "Can not find 'git' command."

# Check that base ref has been specified
[ -n "$1" ] || die "Missing required first parameter: git-base-ref"
BASE="$1"

# Check it is a valid ref
git rev-parse --quiet --verify "$BASE" > /dev/null || die "Specified git base ref '$BASE' is not a valid ref in this repository."

# git uses the following one-character change status codes
# A - Added
# C - Copied
# D - Deleted
# M - Modified
# R - Renamed

# Keep track of how many files failed the copyright check so we can find them all and return 0 if there were none
FAILED=0

# Copy stdout to another file descriptor for logging
exec 3>&1
log() { echo>&3 "$@"; }

# Look for unsupported changes with Broken (B), changed (T), Unmerged (U), or Unknown (X) status
BAD_FILES="$(git diff --name-status --diff-filter=BTUX "$BASE")"

[ -z "$BAD_FILES" ] || {
  echo "‼️ This script ($0) may need fixing to deal with more types of change." >&2
  echo "$BAD_FILES" | sed 's/^/🤯 Unsupported change type: /'
  FAILED=$(( FAILED + $(echo "$BAD_FILES"|wc -l) ))
}

# Print deleted files, just for completeness
git diff --name-only --diff-filter=D "$BASE" | sed 's/^/🫥 Ignoring deleted file: /'


# Function to print each file from stdin to stdout unless it has good copyright
badCopyrightFilter() {
  while read filePath; do
    [ -f "$filePath" ] || die "Cannot check copyright in non-existent file: '$filePath'"
    grep -Eq "SPDX-License-Identifier: Apache-2.0" "$filePath" || {
      log "👿 License identifier not found: $filePath"
      echo "$filePath"
      continue
    }
    yearModified=`git log -1 --pretty=format:%cd --date=format:%Y -- "$filePath"`
    grep -Eq "Copyright $yearModified IBM Corporation and" "$filePath" && log "😅 Copyright OK: $filePath" || {
      existingModifiedYear="$(grep -Eo 'Copyright [0-9]{4} IBM Corporation and' "$filePath" | cut -d ' ' -f 2 )"
      case "$existingModifiedYear" in
        "$yearModified") continue ;;
        "")              log "🤬 No copyright year in '$filePath': expected '$yearModified'." ;;
        *)               log "😡 Wrong copyright year in '$filePath': expected '$yearModified' but found '$existingModifiedYear'." ;;
      esac
      echo "$filePath"
    }
  done
}

echo "Checking added and modified files..."
FAILED=$((FAILED + $(git diff --name-only --find-copies-harder --diff-filter=AM "$BASE" | badCopyrightFilter | wc -l)))


# Renamed (R) and copied (C) files are more complicated.
# They can report as less than 100% identical even when their contents are the same.
# This is apparently due to metadata changes. Shrug.
# So check whether the contents have changed significantly.

# Define how to compare a file against its origin for significant content changes.
# Succeed if there are differences, and print the filename.
isReallyDifferent() { ! git diff --ignore-all-space --quiet "$1" "$2" 2>/dev/null && echo "$2"; }

# Read status, source, and destination as separate records (lines).
# Check the status is R... or C... (otherwise it was parsed incorrectly).
# Then compare the source and dest for significant differences.
# Lastly, if they were different, check them for copyright.
badCopyrightFilter2() {
  while read status && read src && read dst
  do
    case "$status" in
    R*) isReallyDifferent "$BASE:$src" "$dst" || log "🫥 Ignoring renamed file: $src -> $dst" ;;
    C*) isReallyDifferent "$BASE:$src" "$dst" || log "🫥 Ignoring copied file: $src -> $dst" ;;
    *) die "Unexpected status while parsing git diff output: status='$status' src='$src' dst='$dst'" ;;
    esac
  done | badCopyrightFilter
}

echo "Checking renamed and copied files..."
OUTPUT="$(git diff --name-status --find-copies-harder --diff-filter=CR -z "$BASE" 2>/dev/null | tr '\0' '\n')"
FAILED=$((FAILED + $(echo "$OUTPUT"| badCopyrightFilter2 | wc -l)))

exit $FAILED
