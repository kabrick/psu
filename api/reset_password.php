<?php
include 'dbconfig.php';

$email = $_GET['email'];

$sql = "SELECT name, password, username FROM psu_admin WHERE email = '$email'";

$result = $conn->query($sql);

if ($result->num_rows > 0){

	$row = $result->fetch_assoc();
	$password = base64_decode($row['password']);

	$to_email = $email;
	$subject = 'PSU Reset Password';
	$message = "Hello " . $row['name'] . "\n" . "Your username is: " . $row['username'] . "\n" . "Your password is: " . $password . "\n\n" . "Thank you for using the PSU MOBILE APP.";
	$headers = 'From: psumis2018@gmail.com';

	mail($to_email,$subject,$message,$headers);

	echo "1";
} else {
	echo "0";
}

?>