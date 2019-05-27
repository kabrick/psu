<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT psu_id, cover_letter, resume FROM psu_jobs_applications WHERE jobs_id = '$id'";

$arr = [];
$counter = 0;

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row = $result->fetch_assoc()) {

		$psu_id = $row['psu_id'];

		$sql1 = "SELECT name, email, phone, photo FROM psu_admin WHERE psu_id = '$psu_id'";

		$result1 = $conn->query($sql1);
		$result_assoc1 = $result1->fetch_assoc();

		$arr[$counter]['photo'] = $result_assoc1['photo'];
		$arr[$counter]['name'] = $result_assoc1['name'];
		$arr[$counter]['email'] = $result_assoc1['email'];
		$arr[$counter]['phone'] = $result_assoc1['phone'];
		$arr[$counter]['cover_letter'] = $row['cover_letter'];
		$arr[$counter]['cv'] = $row['resume'];

		$counter++;
 	}
} else {
	//
}

echo json_encode($arr);

?>