from django.http import JsonResponse


def test(request):
    return JsonResponse({
        "flag": "1"
    })
