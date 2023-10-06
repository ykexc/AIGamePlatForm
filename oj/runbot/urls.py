from django.urls import path
from runbot.views.fight import fight
from runbot.views.compile import compile
from runbot.views.index import test
urlpatterns = [
    path("", fight, name="index"),
    path("compile/", compile, name="compile"),
    path("idx/", test, name="idx")
]
