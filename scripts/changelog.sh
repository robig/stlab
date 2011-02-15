#!/bin/bash
script_dir=`dirname $0`

VERSION=`$script_dir/get_version.sh`

echo "
   Changelog of StLab $VERSION
 =-=-=-=-=-=-=-=-=-=-=-=-=-=

Please refer to the changes using its issue id (#ID).
You can get detailed informations on each issue using the Redmine: http://robig.net/redmine/projects/stlab

"


$script_dir/wget -q -O - "http://robig.net/redmine/projects/stlab/issues?fixed_version_id=1&format=csv&query_id=2" | \
	awk '{ if( /\r$/ ) { sub("\r$", "<br>"); printf "%s", $0; } else { print } }'| awk -F ',' '{ print "#" $1 "\t" $4 "\t" $6; }'|grep -v "^##"

echo "
Have fun and please provide feedback at the forum:
https://sourceforge.net/apps/phpbb/stlab/

"
