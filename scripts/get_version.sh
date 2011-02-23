#/bin/bash
from_source(){
grep applicationVersion src/net/robig/stlab/StLab.java|cut -d '=' -f 2|cut -d '"' -f 2|tr "[:upper:]" "[:lower:]"
}
if [ "$1" != "" -a "$1" != "production" ]; then
	#append revision number on non production version:
	from_source |sed 's,$,-r,'|tr -d '\n'
	svn info | grep Revision: | cut -d ' ' -f 2
else
	from_source
fi
