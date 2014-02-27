#!/bin/bash

NM=`basename $0`

# check input
if [[ $# -lt 3 ]]; then
  echo "Usage: $NM [start file, filename of form 'name-001.extension'] [background file] [number of steps]"
fi

INPUT=`echo "$1" | awk -F'-001' '{print $1}'`
BACKGROUND="$2"
EXTENSION="${1##*.}"
N=$3

typeset -i i N
for ((i=1;i<=N-1;++i)); do
  OUTPUT=`printf "%s-%03d.%s\n" "$INPUT" $((i+1)) "$EXTENSION"`
  echo -n "Generating \"$OUTPUT\"... "
  convert "$BACKGROUND" \( "$INPUT-001.$EXTENSION" -alpha set -channel A -evaluate set $((100-(100/(N-1))*i))% \) -composite "$OUTPUT"
  echo "done."
done
