#!/bin/bash

#One off config update
echo localhost:*:infovis:infovis:infovis>~/.pgpass

DIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
FILE1=$DIR/Microblogs.csv

#Testing
#FILE1=/home/akennedy/git/infovis/data/Microblogs10.csv
#head $FILE0 > $FILE1


#exit
function loadCSV {
psql -h localhost -U infovis -d infovis -q << EOF 2>> /tmp/$$.err
SET datestyle = "ISO, MDY";
SET client_encoding = 'latin-1';
TRUNCATE TABLE blogs;
copy blogs from '$FILE1' DELIMITERS ',' CSV HEADER;
EOF
}

# One off data clean
#sed -i 's/,\(42\.[0-9]*\) \(93\.[0-9]*\),/,"\1,\2",/;/^[0-9]*,[0-9]*\/[0-9]*\/2011 [^0-9]/d' $FILE1

loadCSV
cat /tmp/$$.err

exit



#	Dataclean discovery

if [ -e $FILE1.bad ]; then
	rm $FILE1.bad
fi

STATUS=1
while [[ $STATUS > 0 ]]; do
	loadCSV
	cat /tmp/$$.err
	grep ERROR /tmp/$$.err 1>/dev/null||STATUS=0
	if [ $STATUS > 0 ]; then
		LINE=`sed -e '1d;s/^.*line \([0-9]*\).*/\1/' /tmp/$$.err`
		echo Error line:$LINE
		sed -e "${LINE}!d" $FILE1 >> $FILE1.bad
		sed -i "${LINE}d"  $FILE1
    fi
    rm /tmp/$$.err
done

exit


#	Database stuff

CREATE INDEX CONCURRENTLY "blogs_ID_idx" ON blogs("ID");
CREATE INDEX CONCURRENTLY "blogs_Created_at_idx" ON blogs("Created_at");
CREATE INDEX "blogs_Location_idx" ON blogs USING gist (box("Location","Location"));

ALTER TABLE blogs ADD COLUMN textsearchable_index_col tsvector;
UPDATE blogs SET textsearchable_index_col = to_tsvector('english', "Text");
CREATE INDEX blogs_textsearch_idx ON blogs USING gin("textsearchable_index_col");


# Examples

SELECT * FROM blogs
    WHERE box("Location","Location") && '(42.23510,93.33484),(42.23512,93.33486)'::box;

SELECT *
FROM blogs
WHERE 1=1
AND box("Location","Location") && '(42.23400,93.33374),(42.23622,93.33596)'::box
AND textsearchable_index_col @@ to_tsquery('flu | cough | sick')
AND "Created_at" BETWEEN '2011-05-18 09:00:00' AND '2011-05-19 08:59:59'
ORDER BY "Created_at" DESC LIMIT 10
;

