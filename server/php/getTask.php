<?php
/* This PHP script gets a task by id.
 * May be extended to get a task by various
 * types of fields, or may have a new file created
 * for each such field.
 */
include 'credentials.php';
$link = mysql_connect($db_host,$db_user,$db_pass);

mysql_select_db($database);

if(!$link) {
  die('Could not connect: ' . mysql_error());
}

$query=mysql_query(
    "SELECT * FROM tasks " .
    "WHERE _id=".$_REQUEST["id"]);

while($i = mysql_fetch_assoc($query))
  $out[]=$i;

print(json_encode($out));

mysql_close();
?>

