<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT name, email, phone, username FROM psu_admin WHERE psu_id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

//get the default info
$responce['name'] = $result_assoc['name'];
$responce['phone'] = $result_assoc['phone'];
$responce['username'] = $result_assoc['username'];
$responce['email'] = $result_assoc['email'];

echo json_encode($responce);

?>