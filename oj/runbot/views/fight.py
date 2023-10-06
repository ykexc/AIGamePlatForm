from django.http import JsonResponse
from runbot.views.sandbox import SandBox
from django.views.decorators.csrf import csrf_exempt
import os
import json
import requests


# Create your views here.

def delete_file(code, _input):
    if os.path.isfile(code) and os.path.isfile(_input):
        os.remove(code)
        os.remove(_input)


def create_file(content, path):
    f = open(path, 'w', encoding='UTF-8')
    f.write(content)
    f.close()


@csrf_exempt
def fight(request):
    body = json.loads(request.body)
    print(body)
    status = body['status']
    container = body['container']
    if status == '2':
        sandbox = SandBox('/root/oj/runbot/code.txt', 'cpp', container, True)
        sandbox.close()
        return JsonResponse({
            'result': 'ok',
        })

    game = body['game']
    random = body['random']
    path = '/root/oj/runbot/usercodes/' + game + random
    code = path + 'code.txt'
    _input = path + 'input.txt'
    codes = body['codes']
    _inputs = body['inputs']
    create_file(codes, code)
    create_file(_inputs, _input)
    url = 'https://ykexc.work/api/pk/game/' + game + '/receiveBot'

    ext = body['language']
    userId = body['userId']

    if status == '0':
        sandbox = SandBox(code, ext, None, False)
    elif status == '1':
        sandbox = SandBox(code, ext, container, True)

    if sandbox is None:
        delete_file(code, _input)
        return JsonResponse({
            'result': 'fail',
            'msg': 'create sandbox file',
        })
    if status == '0':
        try:
            sandbox.create()
            sandbox.compile()
            container = sandbox.container
        except:
            delete_file(code, _input)
            sandbox.close()
            raise RuntimeError('sandbox create or complie fail')

    with open(_input, 'r', encoding='utf-8') as f:
        content = f.read()
    try:
        d = sandbox.run(content, True)
    except:
        delete_file(code, _input)
        sandbox.close()
        raise

    if d['verdict'] != 'OK':
        sandbox.close()

    # print(d)
    if game == 'snake':
        data = {
            "userId": userId,
            "direction": d['output'][0],
            "container": sandbox.container,
        }
    elif game == 'gobang':
        print(url)
        op = d['output']
        nx, ny = op.split('#')
        print('nx:', nx, 'ny:', ny)
        data = {
            "userId": userId,
            "nx": nx,
            "ny": ny,
            "container": sandbox.container,
        }
        # print(d['output'][0], d['output'][1])
    delete_file(code, _input)
    requests.post(url, data)
    return JsonResponse({
        'result': 'ok',
    })
