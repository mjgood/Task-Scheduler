<?php
/* This PHP script gets all Tuples from
 * the new_tasks table, returning an array of
 * JSON tuples.
 */
include '../credentials.php';

$link = mysqli_connect($db_host,$db_user,$db_pass,$database);

header('Content-Type: text/plain; charset=utf-8');

if(!$link)
  die('Could not connect: ' . mysqli_connect_error());

$query = "SELECT * FROM new_tasks ORDER BY task_id ASC;";

$result = mysqli_query($link, $query);

if($result){
  while($i = mysqli_fetch_assoc($result))
    $out[]=$i;
  print(json_encode($out));
}else
  die("ERROR: ".mysqli_error($link));

mysqli_close($link);
?>
