#!/bin/bash
# version 0.1
# emergency StLab Application starter for MacOS

error() {
	echo "ERROR: $1" >&2
	exit 1
}

DIR=`dirname $0`
cd "$DIR" || error "Cannot chdir to $DIR"
cd Contents/Resources/Java || error "Cannot chdir into Contents/Resources/Java"

CLASSPATH=.
for f in *.jar img.zip; do
	CLASSPATH=$CLASSPATH:$f
done

ls -l
java -version
set -x
java -cp $CLASSPATH -Djava.library.path=. net.robig.stlab.StLab
