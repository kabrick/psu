<?php
include 'dbconfig.php';

$email = $_GET['email'];

$sql = "SELECT name FROM psu_admin WHERE email = '$email'";

$result = $conn->query($sql);

if ($result->num_rows > 0){
	$permitted_chars = 'abcdefghijklmnopqrstuvwxyz';
 
	$input_length = strlen($permitted_chars);
    $random_string = '';
    for($i = 0; $i < 8; $i++) {
        $random_character = $permitted_chars[mt_rand(0, $input_length - 1)];
        $random_string .= $random_character;
    }

	$password = base64_encode($random_string);

	$to_email = $email;
	$subject = 'Reset Password';
	$message = "Your new password is " . $random_string;
	$headers = 'From: phasouganda@gmail.com';
	mail($to_email,$subject,$message,$headers);

	$sql = "UPDATE psu_admin SET password='$password' WHERE email = '$email'";

	if ($conn->query($sql) === TRUE) {
	    echo "1";
	} else {
	    echo "2";
	}
} else {
	echo "0";
}

?>