<?php

//tested using SoapUI version 4.5.2
//tested eith php wsdl_client.php
//tested eith c# visual studio 2010
//complex data type not supported yet.
//only support integer string and float data type.
require_once "./wsdl.class.php"; //just include service class to use it
////seguridad
//
//
//
//implement function body 
function testurl($data) {
    return 'well hello ' . $data;
}

//implement function body 
function test2($name, $age) {
    if (!isset($age)) {
        return 'fuck ya';
    }
    if (is_numeric($age)) {
        return 'hello ' . $name . " I know you are $age years old";
    } else {
        return 'fuck';
    }
}

function Version($name, $age) {
    
    return 1.1;
}

$e = new SSoap('Sourena'); //your service name here as argument

$e->register(
        'testurl', //function name of the service
        array(//input arguments of the function as name=>type 
    'username' => 'string',
        ), array(//output arguments of the function as name=>type
    'return' => 'str'
        ), 'this function suppose to be a test'
);
//define another service
$e->register(
        'test2', array(
    'username' => 'stRing',
    'age' => 'float'
        ), array(
    'return' => 'str'
        ), 'this is another test'
);
$e->register(
        'Version', array(
    'username' => 'stRing',
    'age' => 'stRing'
        ), array(
    'return' => 'str'
        ), 'this is another test'
);


$e->handle();  //call this method to start service handle
?>