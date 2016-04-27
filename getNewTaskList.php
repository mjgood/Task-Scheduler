<?php
/* This PHP script gets a list of task_ids from
 * the new_tasks table, returning an array of
 * JSON tuples.
 * Tuples are of the format task_id:<id>
 */
include '../credentials.php';

$link = mysqli_connect($db_host,$db_user,$db_pass,$database);

if(!$link)
  die('Could not connect: ' . mysqli_connect_error());

$query = "SELECT task_id FROM new_tasks LIMIT 10;";

$result = mysqli_query($link, $query);

if($result){
  while($i = mysqli_fetch_assoc($result))
    $out[]=$i;
  print(json_encode($out));
}else
  die("ERROR: ".mysqli_error($link));

mysqli_close();
?>
