<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql_e = "SELECT id FROM psu_pharmacies WHERE update_by='$id'";
$result_e = $conn->query($sql_e);

if($result_e->num_rows > 1){
	echo "0";
} else {
	echo "1";
}

?>