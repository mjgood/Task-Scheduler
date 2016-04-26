<?php
/* This PHP script gets a task by id.
 * May be extended to get a task by various
 * types of fields, or may have a new file created
 * for each such field.
 */
include 'credentials.php';
$link = mysqli_connect($db_host,$db_user,$db_pass,$database);

if(!$link) {
  die('Could not connect: ' . mysqli_connect_error());
}

$id=$_REQUEST["id"]; //No input validation here, careful

$query =
    "SELECT * FROM tasks " .
    "WHERE _id = $id";

$result = mysqli_query($link, $query);

while($i = mysqli_fetch_assoc($result))
  $out[]=$i;

print(json_encode($out));

mysqli_close();
?>

