<?php
include 'dbconfig.php';

$pharmacist = $_GET['pharmacist'];
$pharmacy_id = $_GET['id'];

// check if the pharmacy has already been assigned
$sql = "SELECT psu_id FROM psu_pharm_directors WHERE pharmacy_id = '$pharmacy_id'";

$result = $conn->query($sql);

if ($result->num_rows < 1){
	//insert values
	$sql = "INSERT INTO psu_pharm_directors (psu_id, pharmacy_id) VALUES ('$pharmacist','$pharmacy_id')";

	if ($conn->query($sql) === TRUE) {
		echo "1";
	} else {
	    echo "0";
	}
} else {
	$result_assoc = $result->fetch_assoc();

	$id = $result_assoc['psu_id'];

	$sql1 = "SELECT name, phone FROM psu_admin WHERE psu_id = '$id'";

	$result1 = $conn->query($sql1);
	$result_assoc1 = $result1->fetch_assoc();

	echo "This pharmacy already has an owner assigned to it. Please contact the current assigned owner " . $result_assoc1['name'] . " on " . $result_assoc1['phone'];
}

?>