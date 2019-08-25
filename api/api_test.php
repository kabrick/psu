<?php

/*class Notification{
	private $title;
	private $message;
	private $image_url;
	private $action;
	private $action_destination;
	private $data;
	
	function __construct(){
         //
	}
 
	public function setTitle($title){
		$this->title = $title;
	}
 
	public function setMessage($message){
		$this->message = $message;
	}
 
	public function setImage($imageUrl){
		$this->image_url = $imageUrl;
	}
 
	public function setAction($action){
		$this->action = $action;
	}
 
	public function setActionDestination($actionDestination){
		$this->action_destination = $actionDestination;
	}
 
	public function setPayload($data){
		$this->data = $data;
	}
	
	public function getNotificatin(){
		$notification = array();
		$notification['title'] = $this->title;
		$notification['message'] = $this->message;
		$notification['image'] = $this->image_url;
		$notification['action'] = $this->action;
		$notification['action_destination'] = $this->action_destination;
		return $notification;
	}
}*/

//$firebase_token = $_POST['firebase_token'];
//$firebase_api = $_POST['firebase_api'];

//$topic = $_POST['topic'];

$requestData = array(
		'title' => 'Hello',
		'message' => 'This is a test',
	);

$fields = array(
		'to' => '/topics/test',
		'data' => $requestData,
	);

// Set POST variables
$url = 'https://fcm.googleapis.com/fcm/send';

$headers = array(
	'Authorization: key=AAAAoL1T1jo:APA91bGxWbqTJatZ3oam3_lxAjDmH8GY4F9m-PLMouHytUTgxyxMu2sb1Oq4Il6dLCjE3nw5ExY9tdXpPVYdCnWGEuCCIBZShKrA78zOaUghfg2ehKt1tc33LOG2c2BnErCN7PlCaTYQ',
	'Content-Type: application/json'
);

// Open connection
$ch = curl_init();

// Set the url, number of POST vars, POST data
curl_setopt($ch, CURLOPT_URL, $url);

curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

// Disabling SSL Certificate support temporarily
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

// Execute post
$result = curl_exec($ch);
if($result === FALSE){
	die('Curl failed: ' . curl_error($ch));
}

// Close connection
curl_close($ch);

echo '<h2>Result</h2><hr/><h3>Request </h3><p><pre>';
echo json_encode($fields,JSON_PRETTY_PRINT);
echo '</pre></p><h3>Response </h3><p><pre>';
echo $result;
echo '</pre></p>';

?>