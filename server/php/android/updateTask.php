<?php
/* This PHP script inserts a new row into the database.
 * NOTE: update a task in the task table, given an _id.
 * Requires id and at least one other value POSTed
 */
include '../credentials.php';

header('Content-Type: text/plain; charset=utf-8');

$link = mysqli_connect($db_host,$db_user,$db_pass,$database);

if(!$link) {
  die('Could not connect: ' . mysqli_connect_error());
}

//Pull POST/GET values, prep for insertion
$id = $_REQUEST["id"];
$subject = $_REQUEST["subject"];
$comp_status = $_REQUEST["completion_status"];
$comp_percent = $_REQUEST["completion_percentage"];
$repeat_id = $_REQUEST["repeat_id"];
$start_time = $_REQUEST["start_time"];
$end_time = $_REQUEST["end_time"];
$deadline = $_REQUEST["deadline_time"];
$est_time = $_REQUEST["estimated_time"];
$priority = $_REQUEST["priority"];
$rep_cond = $_REQUEST["repeat_conditions"];
$description = $_REQUEST["description"];

$not_first = 0; //Is this the first element to be inserted?
//Also validates more args have been passed

if(!$id)
  die('ERROR: must pass id');

$query =
    "UPDATE tasks " .
    "SET ";

//subject passed
if(isset($_REQUEST["subject"])){
  $query.=" subject = $subject";
  $not_first=1;
}

//completion_status passed
if(isset($_REQUEST["completion_status"])){
  if($not_first==1)
    $query.=", ";
  $query.=" completion_status = $comp_status";
  $not_first=1;
}

//completion_percentage passed
if(isset($_REQUEST["completion_percentage"])){
  if($not_first==1)
    $query.=", ";
  $query.=" completion_percentage = $comp_percent";
  $not_first=1;
}

//repeat_id passed
if(isset($_REQUEST["repeat_id"])){
  if($not_first==1)
    $query.=", ";
  $query.=" repeat_id = $repeat_id";
  $not_first=1;
}

//start_time passed
if(isset($_REQUEST["start_time"])){
  if($not_first==1)
    $query.=", ";
  $query.=" start_time = $start_time";
  $not_first=1;
}

//end_time passed
if(isset($_REQUEST["end_time"])){
  if($not_first==1)
    $query.=", ";
  $query.=" end_time = $end_time";
  $not_first=1;
}

//deadline_time passed
if(isset($_REQUEST["deadline_time"])){
  if($not_first==1)
    $query.=", ";
  $query.=" deadline_time = $deadline";
  $not_first=1;
}

//estimated_time passed
if(isset($_REQUEST["estimated_time"])){
  if($not_first==1)
    $query.=", ";
  $query.=" estimated_time = $est_time";
  $not_first=1;
}

//priority passed
if(isset($_REQUEST["priority"])){
  if($not_first==1)
    $query.=", ";
  $query.=" priority = $priority";
  $not_first=1;
}

//repeat_conditions passed
if(isset($_REQUEST["repeat_conditions"])){
  if($not_first==1)
    $query.=", ";
  $query.=" repeat_conditions = $rep_cond";
  $not_first=1;
}

//description passed
if(isset($_REQUEST["description"])){
  if($not_first==1)
    $query.=", ";
  $query.=" description = $description";
  $not_first=1;
}

$query.=" WHERE _id = $id;";

if(!$not_first)
  die("ERROR: Must pass other args.<br />QUERY: $query");

$result = mysqli_query($link, $query);

if($result)
  print("true");
else
  die("ERROR: ".mysqli_error($link));

mysqli_close($link);
?>
