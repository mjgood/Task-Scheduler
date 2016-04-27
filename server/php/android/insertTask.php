<?php
/* This PHP script inserts a new row into the database.
 * NOTE: _id is always provided by Android.
 * Requires id, subject to be posted.
 */
include '../credentials.php';
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

if(!$id)
  die('ERROR: must pass id');
if(!$subject)
  die('ERROR: must past subject');

$query =
    "INSERT INTO tasks (";

//_ID passed
if(isset($_REQUEST["id"])){
  $query.="_id";
  $not_first=1;
}

//subject passed
if(isset($_REQUEST["subject"])){
  if($not_first==1)
    $query.=", ";
  $query.="subject";
  $not_first=1;
}

//completion_status passed
if(isset($_REQUEST["completion_status"])){
  if($not_first==1)
    $query.=", ";
  $query.="completion_status";
  $not_first=1;
}

//completion_percentage passed
if(isset($_REQUEST["completion_percentage"])){
  if($not_first==1)
    $query.=", ";
  $query.="completion_percentage";
  $not_first=1;
}

//repeat_id passed
if(isset($_REQUEST["repeat_id"])){
  if($not_first==1)
    $query.=", ";
  $query.="repeat_id";
  $not_first=1;
}

//start_time passed
if(isset($_REQUEST["start_time"])){
  if($not_first==1)
    $query.=", ";
  $query.="start_time";
  $not_first=1;
}

//end_time passed
if(isset($_REQUEST["end_time"])){
  if($not_first==1)
    $query.=", ";
  $query.="end_time";
  $not_first=1;
}

//deadline_time passed
if(isset($_REQUEST["deadline_time"])){
  if($not_first==1)
    $query.=", ";
  $query.="deadline_time";
  $not_first=1;
}

//estimated_time passed
if(isset($_REQUEST["estimated_time"])){
  if($not_first==1)
    $query.=", ";
  $query.="estimated_time";
  $not_first=1;
}

//priority passed
if(isset($_REQUEST["priority"])){
  if($not_first==1)
    $query.=", ";
  $query.="priority";
  $not_first=1;
}

//repeat_conditions passed
if(isset($_REQUEST["repeat_conditions"])){
  if($not_first==1)
    $query.=", ";
  $query.="repeat_conditions";
  $not_first=1;
}

//description passed
if(isset($_REQUEST["description"])){
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
if(isset($_REQUEST["id"])){
  $query.="$id";
  $not_first=1;
}

//subject passed
if(isset($_REQUEST["subject"])){
  if($not_first==1)
    $query.=", ";
  $query.="$subject";
  $not_first=1;
}

//completion_status passed
if(isset($_REQUEST["completion_status"])){
  if($not_first==1)
    $query.=", ";
  $query.="$comp_status";
  $not_first=1;
}

//completion_percentage passed
if(isset($_REQUEST["completion_percentage"])){
  if($not_first==1)
    $query.=", ";
  $query.="$comp_percent";
  $not_first=1;
}

//repeat_id passed
if(isset($_REQUEST["repeat_id"])){
  if($not_first==1)
    $query.=", ";
  $query.="$repeat_id";
  $not_first=1;
}

//start_time passed
if(isset($_REQUEST["start_time"])){
  if($not_first==1)
    $query.=", ";
  $query.="$start_time";
  $not_first=1;
}

//end_time passed
if(isset($_REQUEST["end_time"])){
  if($not_first==1)
    $query.=", ";
  $query.="$end_time";
  $not_first=1;
}

//deadline_time passed
if(isset($_REQUEST["deadline_time"])){
  if($not_first==1)
    $query.=", ";
  $query.="$deadline";
  $not_first=1;
}

//estimated_time passed
if(isset($_REQUEST["estimated_time"])){
  if($not_first==1)
    $query.=", ";
  $query.="$est_time";
  $not_first=1;
}

//priority passed
if(isset($_REQUEST["priority"])){
  if($not_first==1)
    $query.=", ";
  $query.="$priority";
  $not_first=1;
}

//repeat_conditions passed
if(isset($_REQUEST["repeat_conditions"])){
  if($not_first==1)
    $query.=", ";
  $query.="$rep_cond";
  $not_first=1;
}

//description passed
if(isset($_REQUEST["description"])){
  if($not_first==1)
    $query.=", ";
  $query.="$description";
  $not_first=1;
}

$query.=");";

$result = mysqli_query($link, $query);

if($result)
  print("true");
else
  die("ERROR: ".mysqli_error($link));

mysqli_close();
?>
