<?php
include 'dbconfig.php';

$full_names = $_GET['full_names'];
$email = $_GET['email'];
$phone = $_GET['phone'];
$nationality = $_GET['nationality'];
$mem_status = $_GET['mem_status'];

if($mem_status == "pharmdirector"){
	//get the last id
	$sql_id = "SELECT psu_id FROM psu_admin ORDER BY psu_id DESC LIMIT 1";

	$result_id = $conn->query($sql_id);
	$result_assoc_id = $result_id->fetch_assoc();
	$psu_id = $result_assoc_id['psu_id'];

	$psu_id = $psu_id + 1;

	$date = date("Y-m-d");
	$password = $email . $psu_id;

	$password = base64_encode($password);

	//insert values
	$sql = "INSERT INTO psu_admin (psu_id, name, email, phone, username, password, type, status, date_created) VALUES ('$psu_id','$full_names','$email','$phone','$email','$password','$mem_status','active','$date')";

	if ($conn->query($sql) === TRUE) {
	    echo "1";
	} else {
	    echo "0";
	}
} else {
	$sql_email = "SELECT psu_id FROM psu_admin WHERE email = '$email'";

	$result_email = $conn->query($sql_email);

	if ($result_email->num_rows > 0){
		$result_assoc_email = $result_email->fetch_assoc();
		$psu_id = $result_assoc_email['psu_id'];

		$password = $email . $psu_id;

		$password = base64_encode($password);

		$sql = "UPDATE psu_admin SET username = '$email', password = '$password' WHERE psu_id = '$psu_id'";

		if ($conn->query($sql) === TRUE) {
		    echo "1";
		} else {
		    echo "0";
		}
	} else {
		//email address does not exist
		echo "2";
	}
}

?>