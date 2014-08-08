import web
import json
import bifubao

urls = (
  '/'        , 'index',
  '/request' ,  'request',
  '/callback', 'callback'
)

class index:
  def GET(self):
    render = web.template.render('templates')
    return render.index()

class request:
  def POST(self):
    bifubaoTool = bifubao.bifubao()
    render = web.template.render('templates')
    data = web.input()
    return render.layout(bifubaoTool.createPayForm(data))

class callback:
  def POST(self):
    bifubaoTool = bifubao.bifubao()
    data = web.input()
    print data
    if not bifubaoTool.bifubaoCallbackVerify(data):
      return 'verify faild'
    print 'verify success'
    content = json.loads(data.content)
    if content['handle_status'] < 1000:
      #todo onchan支付金额不足时逻辑
      print 'pay btc not enough'
    else:
      #todo 支付成功的逻辑
      print 'pay success'
    return web.input()['_request_check_']

if __name__ == "__main__": 
    app = web.application(urls, globals())
    app.run()