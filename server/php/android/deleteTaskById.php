<?php
/* This PHP script deletes a task by id.
 * May be modified to delete based upon other
 * POST parameters in the future.
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

//If we are deleting a new task:
if($new_task){
  $query =
      "DELETE FROM new_tasks " .
      "WHERE task_id = $id";
}else{ //If we are deleting a previously snyced task:
  $query =
      "DELETE FROM tasks " .
      "WHERE _id = $id";
}

$result = mysqli_query($link, $query);

if($result)
  print("true");
else
  die("ERROR: ".mysqli_error($link));

mysqli_close($link);
?>
