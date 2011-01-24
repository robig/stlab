#!/bin/bash
# version 0.1
# emergency StLab Application starter for MacOS

JAVA=/opt/java/jre1.5.0_22/bin/java

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
$JAVA -version
set -x
$JAVA -cp $CLASSPATH -Djava.library.path=. net.robig.stlab.StLab
