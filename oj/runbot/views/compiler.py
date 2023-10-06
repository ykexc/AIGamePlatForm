config = dict(
    cpp = dict(
        suffix = 'cpp',
        images = ['botzone/env-cpp'],
        time_limit = 1000,
        sub_time_limit = 1000,
        memory_limit = 256,
        compile_command = 'g++ -D_BOTZONE_ONLINE -D_GLIBCXX_USE_CXX11_ABI=0 -O2 -Wall -mavx -std=c++11 -x c++ {code} -ljson -lpthread -o {target}',
        run_command = '{target}'
    ),
    java = dict(
        suffix = 'java',
        images = ['botzone/env-java'],
        time_limit = 3000,
        sub_time_limit = 2000,
        memory_limit = 256,
        compile_command = 'javac -encoding utf-8 {code}',
        run_command = '/usr/bin/java Main'
    ),
    python = dict(
        suffix = 'py',
        images = ['botzone/env-py'],
        time_limit = 6000,
        sub_time_limit = 4000,
        memory_limit = 256,
        compile_command = None,
        run_command = '/usr/bin/python {target}'
    ),
    python3 = dict(
        suffix = 'py',
        images = ['botzone/env-py36'],
        time_limit = 3000,
        sub_time_limit = 2000,
        memory_limit = 256,
        compile_command = None,
        run_command = '/usr/bin/python3.6 {target}'
    ),
)

from collections import defaultdict
images = defaultdict(list)
for ext in config:
    for image in config[ext]['images']:
        images[image].append(ext)


# python manage.py runserver 0.0.0.0:8000