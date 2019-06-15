<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT * FROM psu_ecpd WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

$id = $result_assoc['author_id'];

//get the author full name
$sql1 = "SELECT name FROM psu_admin WHERE psu_id = '$id'";

$result1 = $conn->query($sql1);
$result_assoc1 = $result1->fetch_assoc();

//get the default info
$responce['author_id'] = $result_assoc['author_id'];
$responce['title'] = $result_assoc['title'];
$responce['description'] = $result_assoc['description'];
$responce['author'] = $result_assoc1['name'];
$responce['timestamp'] = $result_assoc['timestamp'];
$responce['has_test'] = $result_assoc['has_test'];
$responce['resource_type'] = $result_assoc['resource_type'];
$responce['resource_text'] = $result_assoc['resource_text'];
$responce['resource_url'] = $result_assoc['resource_url'];
$responce['tests_taken'] = $result_assoc['tests_taken'];
$responce['success_rate'] = $result_assoc['success_rate'];
$responce['id'] = $result_assoc['id'];

echo json_encode($responce);

?>