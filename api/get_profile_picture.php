<?php
include 'dbconfig.php';

$psu_id = $_GET['id'];

$responce = array();

$sql = "SELECT photo FROM psu_admin WHERE psu_id = '$psu_id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

//get the default info
$responce['photo'] = $result_assoc['photo'];

echo json_encode($responce);

?>