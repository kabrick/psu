<?php
include 'dbconfig.php';

$username = $_GET['username'];
$password = $_GET['password'];

$password = base64_encode($password);

$sql = "SELECT psu_id,type, name FROM psu_admin WHERE password = '$password' AND username = '$username'";

$result = $conn->query($sql);

if ($result->num_rows > 0){
	$result_assoc = $result->fetch_assoc();
	$psu_id = $result_assoc['psu_id'];
	$name = $result_assoc['name'];
	$type = $result_assoc['type'];

	if ($type == 'admin'){
		$type_value = '1';
	} else if ($type == 'pharmacists'){
		$type_value = '2';
	} else if ($type == 'pharmdirector'){
		$type_value = '3';
	} else if ($type == 'internpharma'){
		$type_value = '4';
	} else {
		$type_value = '0';
	}

	$return_string = $psu_id . "," . $type_value . "," . $name;
	echo $return_string;
} else {
	echo "0";
}

?>