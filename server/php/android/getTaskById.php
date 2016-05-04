<?php
/* This PHP script gets a task by id.
 * May be extended to get a task by various
 * types of fields, or may have a new file created
 * for each such field.
 */
include '../credentials.php';

$id = $_REQUEST["id"];
$new_task = (isset($_REQUEST["new_task"]))?$_REQUEST["new_task"]:false;

header('Content-Type: text/plain; charset=utf-8');

if(!$id)
  die('ERROR: must pass id');

$link = mysqli_connect($db_host,$db_user,$db_pass,$database);

if(!$link) {
  die('Could not connect: ' . mysqli_connect_error());
}

//If we are polling for a new task:
if($new_task){
  $query =
      "SELECT * FROM new_tasks " .
      "WHERE task_id = $id";
}else{ //If we are polling for a previously snyced task:
  $query =
      "SELECT * FROM tasks " .
      "WHERE _id = $id";
}

$result = mysqli_query($link, $query);

if($result){
  while($i = mysqli_fetch_assoc($result))
    $out[]=$i;
  print(json_encode($out));
}else
  die("ERROR: ".mysqli_error($link));

mysqli_close($link);
?>
