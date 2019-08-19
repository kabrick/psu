<?php
include 'dbconfig.php';

$responce = array();

$sql = "SELECT * FROM psu_settings WHERE id = 1";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

$responce['passmark'] = $result_assoc['test_pass_mark'];
$responce['current_version'] = $result_assoc['current_version'];

echo json_encode($responce);

?>