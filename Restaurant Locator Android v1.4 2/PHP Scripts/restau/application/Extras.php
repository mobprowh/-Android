<?php

class Extras
{
 
    function __construct()  { }
 
    function __destruct() { }
 
    function encryptQuery1($keySalt, $qry, $val, $landing_page){

         //making query string
        $qryStr = "$qry=".$val; 

        //this line of code encrypt the query string
        $query = base64_encode(
                        urlencode( 
                                mcrypt_encrypt(
                                    MCRYPT_RIJNDAEL_256, 
                                    md5($keySalt), 
                                    $qryStr, 
                                    MCRYPT_MODE_CBC, 
                                    md5(md5($keySalt))
                                )
                            )
                        );    

        $link = "$landing_page?".$query;

        return $link;
    }

    function decryptQuery1($keySalt, $qryStr){

        //this line of code decrypt the query string
        $queryString = rtrim(
                            mcrypt_decrypt(
                                    MCRYPT_RIJNDAEL_256, 
                                    md5($keySalt), 
                                    urldecode(base64_decode($qryStr)), 
                                    MCRYPT_MODE_CBC, 
                                    md5(md5($keySalt))), 
                                    "\0");   

        parse_str($queryString);

        $val = explode('=', $queryString);

        $count = count($val);

        if($count == 2)
            return $val[1];

        return null;
    }


    function encryptQuery2($keySalt, $qry1, $val1, $qry2, $val2, $landing_page){

        //making query string
        $qryStr = "$qry1=".$val1."&$qry2=".$val2;  

        //this line of code encrypt the query string
        $query = base64_encode(
                        urlencode( 
                                mcrypt_encrypt(
                                    MCRYPT_RIJNDAEL_256, 
                                    md5($keySalt), 
                                    $qryStr, 
                                    MCRYPT_MODE_CBC, 
                                    md5(md5($keySalt))
                                )
                            )
                        );    

        $link = "$landing_page?".$query;

        return $link;
    }


    function decryptQuery2($keySalt, $qryStr){

        //this line of code decrypt the query string
        $queryString = rtrim(
                            mcrypt_decrypt(
                                    MCRYPT_RIJNDAEL_256, 
                                    md5($keySalt), 
                                    urldecode(base64_decode($qryStr)), 
                                    MCRYPT_MODE_CBC, 
                                    md5(md5($keySalt))), 
                                    "\0");   

        parse_str($queryString);

        $amp = explode('&', $queryString);
        $ampCount = count($amp);
        
        if($ampCount == 2) {
            $equal1 = explode('=', $amp[0]);
            $equal2 = explode('=', $amp[1]);

            $equalCount1 = count($equal1);
            $equalCount2 = count($equal2);

            if($equalCount1 == 2 && $equalCount2 == 2) {
                $val = array();
                $val[0] = $equal1[1];
                $val[1] = $equal2[1];

                return $val;
            }

        }

        return null;
    }


    function removeHttp($str) {
        
        $prefix = 'http://';
        if (substr($str, 0, strlen($prefix)) == $prefix) {
            $str = substr($str, strlen($prefix));
        }

        $prefix = 'https://';
        if (substr($str, 0, strlen($prefix)) == $prefix) {
            $str = substr($str, strlen($prefix));
        }

        return $str;
    }

}
 
?>