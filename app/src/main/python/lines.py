import io
from numpy import asarray
import tensorflow as tf
from PIL import Image, ImageDraw
import numpy as np
from os.path import dirname, join
def make_bezier(xys):
    n = len(xys)
    combinations = pascal_row(n-1)
    def bezier(ts):
        # This uses the generalized formula for bezier curves
        # http://en.wikipedia.org/wiki/B%C3%A9zier_curve#Generalization
        result = []
        for t in ts:
            tpowers = (t**i for i in range(n))
            upowers = reversed([(1-t)**i for i in range(n)])
            coefs = [c*a*b for c, a, b in zip(combinations, tpowers, upowers)]
            result.append(
                tuple(sum([coef*p for coef, p in zip(coefs, ps)]) for ps in zip(*xys)))
        return result
    return bezier

def pascal_row(n, memo={}):
    # This returns the nth row of Pascal's Triangle
    if n in memo:
        return memo[n]
    result = [1]
    x, numerator = 1, n
    for denominator in range(1, n//2+1):
        # print(numerator,denominator,x)
        x *= numerator
        x /= denominator
        result.append(x)
        numerator -= 1
    if n&1 == 0:
        # n is even
        result.extend(reversed(result[:-1]))
    else:
        result.extend(reversed(result))
    memo[n] = result
    return result
def circle1(x1, y1, x2, y2, x3, y3, c):
    A = np.array([[2*(x1 - x2), 2*(y1 - y2)], [2*(x2 - x3), 2*(y2 - y3)]])
    B = np.array([x1**2 + y1**2 - x2**2 - y2**2, x2**2 + y2**2 - x3**2 - y3**2])
    centre = np.linalg.solve(A, B)
    x, y = centre[0], centre[1]
    r = np.sqrt((x-x1)**2 + (y-y1)**2) + c
    return (x - r, y), (x + r, y), (x, y - r - 75)

def circle2(x1, y1, x2, y2, x3, y3, c):
    A = np.array([[2*(x1 - x2), 2*(y1 - y2)], [2*(x2 - x3), 2*(y2 - y3)]])
    B = np.array([x1**2 + y1**2 - x2**2 - y2**2, x2**2 + y2**2 - x3**2 - y3**2])
    centre = np.linalg.solve(A, B)
    x, y = centre[0], centre[1]
    r = np.sqrt((x-x1)**2 + (y-y1)**2) + c
    return (x - r, y - r), (x + r, y + r)

def curve1(l, p, ts, draw):
    xys = [p[i] for i in l]
    bezier = make_bezier(xys)
    points = bezier(ts)
    draw.polygon(points)


def curve2(l, ts, draw):
    bezier = make_bezier(l)
    points = bezier(ts)
    draw.polygon(points)

def plot(im, data):
    #print(data)
    n = 200
    draw = ImageDraw.Draw(im)
    ts = [t / float(n) for t in range(n + 1)]
    p = list(zip(data[0::2], data[1::2]))

    c1 = circle1(*p[21], *p[15], *p[67], 2.5)  # for everything else
    c2 = circle2(*p[21], *p[15], *p[67], 2.5)

    '''c1 = circle1(*p[1], *p[72], *p[67], 5)
    c2 = circle2(*p[1], *p[72], *p[67], 5) '''  # for jpgc not flipped

    curve2([c1[0], *p[0:15], c1[1]], ts, draw)  # jawline

    curve1([27, 68, 28, 69, 37, 70, 30, 71], p, ts, draw)  # left eye

    curve1([32, 72, 33, 73, 45, 74, 35, 75], p, ts, draw)  # right eye

    curve1([22, 23, 24, 24, 23, 22], p, ts, draw)
    curve1([16, 17, 18, 18, 17, 16], p, ts, draw)
    curve2([p[7], p[67], c1[2], c1[2], p[67], p[7]], ts, draw)
    curve1([37, 38, 51, 44, 45], p, ts, draw)
    curve1([44, 43, 42, 47, 67, 67, 47, 42, 43, 44], p, ts, draw)
    curve1([38, 39, 40, 46, 67, 67, 46, 40, 39, 38], p, ts, draw)
    curve1([i for i in range(48, 55)], p, ts, draw)
    curve1([i for i in range(54, 60)] + [48], p, ts, draw)
    draw.ellipse(c2)
    return im



def addLines(x, y):
    bytez = bytes(x)
    im = Image.open(io.BytesIO(bytez))
    y = np.asarray(y)
    new_img = im.resize((360, 480))
    im = plot(new_img, 3*np.array(y[0]))
    imgByteArr = io.BytesIO()
    im.save(imgByteArr, format='PNG')
    imgByteArr = imgByteArr.getvalue()
    return imgByteArr

def onlyLines(x, y):
    bytez = bytes(x)
    im = Image.open(io.BytesIO(bytez))
    im = Image.new('RGB', im.size, (0, 0, 0))
    y = np.asarray(y)
    new_img = im.resize((360, 480))
    im = plot(new_img, 3*np.array(y[0]))
    imgByteArr = io.BytesIO()
    im.save(imgByteArr, format='PNG')
    imgByteArr = imgByteArr.getvalue()
    return imgByteArr