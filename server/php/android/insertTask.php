<?php
/* This PHP script inserts a new row into the database.
 * NOTE: Need to actively sync auto_increment value when possible.
 * Need to determine how to handle that in android.
 */
include '../credentials.php';
$link = mysqli_connect($db_host,$db_user,$db_pass,$database);

if(!$link) {
  die('Could not connect: ' . mysqli_connect_error());
}

//Pull POST/GET values, prep for insertion
$id = (isset($_REQUEST["id"])?$_REQUEST["id"]:false);
$subject = (isset($_REQUEST["subject"])?$_REQUEST["subject"]:false);
$comp_status = (isset($_REQUEST["completion_status"])?$_REQUEST["completion_status"]:false);
$comp_percent = (isset($_REQUEST["completion_percentage"])?$_REQUEST["completion_percentage"]:false);
$repeat_id = (isset($_REQUEST["repeat_id"])?$_REQUEST["repeat_id"]:false);
$start_time = (isset($_REQUEST["start_time"])?$_REQUEST["start_time"]:false);
$end_time = (isset($_REQUEST["end_time"])?$_REQUEST["end_time"]:false);
$deadline = (isset($_REQUEST["deadline_time"])?$_REQUEST["deadline_time"]:false);
$est_time = (isset($_REQUEST["estimated_time"])?$_REQUEST["estimated_time"]:false);
$priority = (isset($_REQUEST["priority"])?$_REQUEST["priority"]:false);
$rep_cond = (isset($_REQUEST["repeat_conditions"])?$_REQUEST["repeat_conditions"]:false);
$description = (isset($_REQUEST["description"])?$_REQUEST["description"]:false);

$not_first = 0; //Is this the first element to be inserted?

$query =
    "INSERT INTO tasks (";

//_ID passed
if($id){
  $query.="_id";
  $not_first=1;
}

//subject passed
if($subject){
  if($not_first==1)
    $query.=", ";
  $query.="subject";
  $not_first=1;
}

//completion_status passed
if($comp_status){
  if($not_first==1)
    $query.=", ";
  $query.="completion_status";
  $not_first=1;
}

//completion_percentage passed
if($comp_percent){
  if($not_first==1)
    $query.=", ";
  $query.="completion_percentage";
  $not_first=1;
}

//repeat_id passed
if($repeat_id){
  if($not_first==1)
    $query.=", ";
  $query.="repeat_id";
  $not_first=1;
}

//start_time passed
if($start_time){
  if($not_first==1)
    $query.=", ";
  $query.="start_time";
  $not_first=1;
}

//end_time passed
if($end_time){
  if($not_first==1)
    $query.=", ";
  $query.="end_time";
  $not_first=1;
}

//deadline_time passed
if($deadline){
  if($not_first==1)
    $query.=", ";
  $query.="deadline_time";
  $not_first=1;
}

//estimated_time passed
if($est_time){
  if($not_first==1)
    $query.=", ";
  $query.="estimated_time";
  $not_first=1;
}

//priority passed
if($priority){
  if($not_first==1)
    $query.=", ";
  $query.="priority";
  $not_first=1;
}

//repeat_conditions passed
if($rep_cond){
  if($not_first==1)
    $query.=", ";
  $query.="repeat_conditions";
  $not_first=1;
}

//description passed
if($description){
  if($not_first==1)
    $query.=", ";
  $query.="description";
  $not_first=1;
}

//VALUES
$query.=") VALUES (";
//Reset $not_first
$not_first=0;

//_ID passed
if($id){
  $query.="$id";
  $not_first=1;
}

//subject passed
if($subject){
  if($not_first==1)
    $query.=", ";
  $query.="$subject";
  $not_first=1;
}

//completion_status passed
if($comp_status){
  if($not_first==1)
    $query.=", ";
  $query.="$comp_status";
  $not_first=1;
}

//completion_percentage passed
if($comp_percent){
  if($not_first==1)
    $query.=", ";
  $query.="$comp_percent";
  $not_first=1;
}

//repeat_id passed
if($repeat_id){
  if($not_first==1)
    $query.=", ";
  $query.="$repeat_id";
  $not_first=1;
}

//start_time passed
if($start_time){
  if($not_first==1)
    $query.=", ";
  $query.="$start_time";
  $not_first=1;
}

//end_time passed
if($end_time){
  if($not_first==1)
    $query.=", ";
  $query.="$end_time";
  $not_first=1;
}

//deadline_time passed
if($deadline){
  if($not_first==1)
    $query.=", ";
  $query.="$deadline";
  $not_first=1;
}

//estimated_time passed
if($est_time){
  if($not_first==1)
    $query.=", ";
  $query.="$est_time";
  $not_first=1;
}

//priority passed
if($priority){
  if($not_first==1)
    $query.=", ";
  $query.="$priority";
  $not_first=1;
}

//repeat_conditions passed
if($rep_cond){
  if($not_first==1)
    $query.=", ";
  $query.="$rep_cond";
  $not_first=1;
}

//description passed
if($description){
  if($not_first==1)
    $query.=", ";
  $query.="$description";
  $not_first=1;
}

$query.=");";

$result = mysqli_query($link, $query);

print("\n");
if($result)
  print("true");
else
  print("ERROR: ".mysqli_error($link));

mysqli_close();
?>
