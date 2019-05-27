<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT photo FROM psu_jobs WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

// check if photo exists
if(!is_null($result_assoc['photo'])){
	$responce['photo'] = $result_assoc['photo'];
} else {
	$responce['photo'] = 0;
}

echo json_encode($responce);

?>