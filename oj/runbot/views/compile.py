from django.http import JsonResponse
from runbot.views.sandbox import SandBox
from django.views.decorators.csrf import csrf_exempt
from random import randint
import os
import json
import requests


# Create your views here.

def delete_file(path):
    if os.path.isfile(path):
        os.remove(path)


def get_random():
    res = ""
    for i in range(8):
        res += str(randint(0, 9))
    return res


@csrf_exempt
def compile(request):
    random = get_random()
    body = json.loads(request.body)
    game = body['game']
    path = '/root/oj/runbot/' + game + '_'

    f2 = open(path + random + 'verify.txt', 'w', encoding='UTF-8')
    f2.write(body['code'])
    f2.close()

    language = body['language']
    sandbox = SandBox(path + random + 'verify.txt', language, None, False)

    if sandbox is None:
        return JsonResponse({
            "result": "fail",
            "msg": "create sandbox fail",
        })

    try:
        sandbox.create()
        sandbox.compile()
    except:
        delete_file(path + random + 'verify.txt')
        sandbox.close()

    if sandbox.container is None:
        delete_file(path + random + 'verify.txt')
        return JsonResponse({
            "result": "fail",
            "msg": "compile error",
            "output": "编译错误"
        })
    with open(path + 'verify_input.txt', encoding='utf-8') as f:
        content = f.read()
    try:
        d = sandbox.run(content, True)
    except:
        delete_file(path + random + 'verify.txt')
        sandbox.close()
    print(d)
    if d['verdict'] != 'OK':
        sandbox.close()
        delete_file(path + random + 'verify.txt')
        return JsonResponse({
            "result": "fail",
            "msg": d['verdict'],
            "output": d['output'],
        })
    sandbox.close()
    delete_file(path + random + 'verify.txt')
    return JsonResponse({
        'result': 'ok',
    })
