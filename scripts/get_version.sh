#/bin/bash
grep applicationVersion src/net/robig/stlab/StLab.java|cut -d '=' -f 2|cut -d '"' -f 2|tr "[:upper:]" "[:lower:]"|sed 's,$,-r,'|tr -d '\n'
if [ "$1" != "" && "$1" != "production" ]; then
	svn info | grep Revision: | cut -d ' ' -f 2
else
	echo
fi
