#/bin/bash
grep applicationVersion src/net/robig/stlab/StLab.java|cut -d '=' -f 2|cut -d '"' -f 2|tr "[:upper:]" "[:lower:]"|sed 's,$,-r,'|tr -d '\n'
svn info | grep Revision: | cut -d ' ' -f 2
