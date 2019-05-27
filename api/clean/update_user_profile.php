<?php
include 'dbconfig.php';

$name = $_GET['name'];
$username = $_GET['username'];
$email = $_GET['email'];
$phone = $_GET['phone'];
$psu_id = $_GET['psu_id'];

//update values
$sql = "UPDATE psu_admin SET username='$username',name='$name',email='$email',phone='$phone' WHERE psu_id = '$psu_id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>