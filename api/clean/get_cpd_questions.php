<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT * FROM psu_ecpd_questions WHERE ecpd_id = '$id'";

$arr = [];
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {

		$arr[$counter]['text'] = $row['question'];
		$arr[$counter]['id'] = $row['id'];

		$counter++;
 	}
}

echo json_encode($arr);

?>