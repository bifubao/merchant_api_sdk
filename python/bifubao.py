#!/usr/bin/env python
# encoding: utf-8
'''
Created on 2014-08
@author: panlilu@gmail.com
'''
import hashlib
import urllib2
import time
import base64

gateway="https://www.bifubao.com/merchant-api/order"

bifubao_pubkey = """-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqUSnx8dqJ0UC0jvFTEdL
gde7BSmKi8GzDnxvu/AMQw7TG3pRKAAKQJRYUSqpgMyOwUSrv3yfu3gBJwufjWJz
Kgtm8D9TOoYnZMJm4x5Lv9/EpYEg0zrAsmU/6rZJ9mYRaNPrt811Thju0/19fa77
XnsQ78UmvV4zCePkKAArO70SsU/hf1SinDX//t0a3/UOk0DhKoJZpzjb5mb+dcXM
GOJKpAONDGDK2UE1W67HmIG72b/R/G8CAFYbw4MGCjb0/Ee6obcAGK3Cj1JcuHjH
NzymBH0NuDvyz7fJuTg9Eplnh1blNeCJoG/vv7VLZNKetTMTx+H2X534RUQ4XheX
4QIDAQAB
-----END PUBLIC KEY-----""";

class bifubao:
    def __init__(self,
                 pid = "80005557777",
                 key = "0dab80d90e477ae97c947468fbc8be53"):
            self.key=key;
            self.conf={
              '_pid_'           :   pid,
            }

    def populateSignStr(self,params):
        ks=params.keys()
        ks.sort()
        rlt=''
        for k in ks:
            if k=="_checksum_" or k=="_signature_sha1_" or k == "_signature_":
              continue
            rlt=rlt+"%s%s"%(k,params[k])
        print "SignStr:"+rlt
        return rlt
        

    def buildSign(self,params):
        sign=hashlib.md5(self.populateSignStr(params)+self.key).hexdigest()
        #print "md5 sign is %s" % sign;
        return sign
    
    
    '''
      校验币比宝返回的参数，交易成功的通知回调.
      
      params为币付宝传回的数据。
    '''
    def bifubaoCallbackVerify(self,params):
        from Crypto.Signature import PKCS1_v1_5
        from Crypto.PublicKey import RSA 
        from Crypto.Hash import SHA

        key = RSA.importKey(bifubao_pubkey)
        h = SHA.new(self.populateSignStr(params))
        verifier = PKCS1_v1_5.new(key) # 采用 SHA1 签名验证
        signature = base64.b64decode(params['_signature_sha1_'])
        return verifier.verify(h, signature)


    '''
        生成提交到币付宝的表单，用户通过此表单将订单信息提交到币付宝。
    '''
    def createPayForm(self,params):
        params.update(self.conf)
        params['_time_'] = time.time()
        sign=self.buildSign(params)
        params['_checksum_']=sign
        
        ele=""
        for nm in params:
           
            print "key in params : %s"%nm
            
            ele = ele + " <input type='hidden' name='%s' value='%s' />" % (nm,params[nm])
        html='''
            <form id="bifubaosubmit" name='bifubaosubmit' action='%s' method="POST">
                %s
                <input type="submit"/>
            </form>
            <script>document.forms['bifubaosubmit'].submit();</script>
            ''' % (gateway,ele)
            
        return html
