<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT pharmacy, location FROM psu_pharmacies WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

//get the default info
$responce['pharmacy'] = $result_assoc['pharmacy'];
$responce['location'] = $result_assoc['location'];

echo json_encode($responce);

?>