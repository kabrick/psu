<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT latitude, longitude FROM psu_pharm_cordinates WHERE pharmacy_id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

//get the default info
$responce['latitude'] = $result_assoc['latitude'];
$responce['longitude'] = $result_assoc['longitude'];

echo json_encode($responce);

?>