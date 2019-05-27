<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT type FROM psu_admin WHERE psu_id = '$id'";

$result = $conn->query($sql);

if ($result->num_rows > 0){
	$result_assoc = $result->fetch_assoc();
	$type = $result_assoc['type'];

	if($type == 'pharmdirector'){
		$returnValue = '1';
	} else if ($type == 'admin'){
		$returnValue = '2';
	} else if ($type == 'pharmacists'){
		$returnValue = '3';
	} else if ($type == 'nda_supervisor'){
		$returnValue = '4';
	} else {
		$returnValue = '0';
	}

	echo $returnValue;
} else {
	echo "0";
}

?>