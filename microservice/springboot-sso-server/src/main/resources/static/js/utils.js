var ajax = function (options) {
    options = {
        type: options.type || "GET",
        url: options.url || "",
        async: options.async === false ? false : true,
        timeout: options.timeout || 5000,
        data: options.data || null,
        username: options.username || null,
        password: options.password || null,
        onSuccess: options.onSuccess || function () {
        },
        onComplete: options.onComplete || function () {
        },
        onError: options.onError || function () {
        },
        typeJson: options.typeJson || {
            "GET": "GET",
            "POST": "POST",
            "PUT": "PUT",
            "PATCH": "PATCH",
            "DELETE": "DELETE"
        },
        requestHeader: options.requestHeader
            || {
                "Content-type": "application/x-www-form-urlencoded; charset=UTF-8"
            },
        overrideMimeType: options.overrideMimeType || null // "text/plain; charset=utf-8"
    };
    if (options.data && typeof options.data === "string") {
        try {
            options.data = JSON.parse(options.data);
        } catch (e) {
        }
    }
    var xhr = getXMLHttpRequest(options.url);
    xhr.open(options.type, options.url, options.async, options.username,
        options.password);
    setRequestHeader(options.requestHeader, xhr);
    if (options.overrideMimeType) {
        xhr.overrideMimeType(options.overrideMimeType);
    }
    var isTimeout = false;
    setTimeout(function () {
        isTimeout = true;
    }, options.timeout);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== 4 && !isTimeout) {
            return;
        }
        if (isSuccess(xhr)) {
            var contentType = xhr.getResponseHeader("content-type");
            var isXml = !options.type && contentType
                && contentType.indexOf("xml") >= 0;
            var data = options.type === "xml" || isXml ? xhr.responseXML
                : xhr.responseText;
            //options.onSuccess.call(this, data);
            var jsonObj = data;
            if ((contentType && contentType.indexOf("json") > 0)
                || options.type.indexOf("json")) {
                try {
                    jsonObj = JSON.parse(data);
                } catch (e) {
                    jsonObj = data;
                }
            }
            options.onSuccess(jsonObj, xhr);
        } else {
            if (options.onError) {
                //options.onError.call(this, xhr);
                options.onError(xhr);
            }
        }
        options.onComplete(xhr);
        xhr = null;
    };
    var data = formatData(options);
    window.setTimeout(function () {
        xhr.send(data);
    }, 0);

    function getXMLHttpRequest(url) {
        var xhr = null;
        try {
            if (url && !isLocal(url)) {
                xhr = new XDomainRequest();
            }
            xhr = xhr || new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e1) {
            try {
                xhr = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e2) {
                xhr = new XMLHttpRequest();
            }
        }
        return xhr;
    }

    function setRequestHeader(requestHeader, xhr) {
        for (var key in requestHeader) {
            xhr.setRequestHeader(key, requestHeader[key]);
        }
        return this;
    }

    var isLocal = function (url) {
        var a = document.createElement('a');
        a.href = url;
        return a.hostname === window.location.hostname;
    };

    function isSuccess(xhr) {
        try {
            return (!xhr.status && location.protocol === "file:")
                || (xhr.status >= 200 && xhr.status < 300)
                || xhr.status === 304
                || (navigator.userAgent.indexOf("Safari") >= 0 && typeof xhr.status === "undefined");
        } catch (e) {
            return false;
        }
    }

    function formatData(options) {
        if (options.data && typeof options.data === 'object') {
            var dataStr = "";
            options.data["_method"] = options.typeJson[options.type];
            for (var key in options.data) {
                dataStr += key + "="
                    + encodeURIComponent(options.data[key]) + "&";
            }
            if (dataStr) {
                dataStr = dataStr.substr(0, dataStr.length - 1);
            }
            return dataStr;
        }
        return options.data;
    }
};


"use strict";


/**
 *
 * @param sTargetStr {String}
 * @returns {number}
 */
function calculateByte(sTargetStr) {
    var sTmpStr = sTargetStr;
    var sTmpChar = "";
    var nOriginLen = sTmpStr.length;
    var nStrLength = 0;
    var i = 0;
    for (; i < nOriginLen; i++) {
        sTmpChar = sTmpStr.charAt(i);
        if (encodeURIComponent(sTmpChar).length > 4) {
            nStrLength += 2;
        } else if (sTmpChar !== '\r') {
            nStrLength += 1;
        }
    }
    return nStrLength;
}

/**
 * var str = "This {0} is test. {1}";
 * str = str.formatStr(["func", "ok"]);
 * @param args {Array}
 * @returns {string}
 */
String.prototype.formatStr = function (args) {
    var str = this;
    var regex = "";
    if (this.formatStrRegExp) {
        regex = this.formatStrRegExp;
    } else {
        String.prototype.formatStrRegExp = /{-?[0-9]+}/g;
        regex = this.formatStrRegExp;
    }

    return str.replace(regex, function (item) {
        var intVal = Number(item.substring(1, item.length - 1));
        var replace = "";
        if (intVal >= 0) {
            replace = args[intVal];
        } else if (intVal === -1) {
            replace = "{";
        } else if (intVal === -2) {
            replace = "}";
        } else {
            replace = "";
        }
        return replace !== undefined ? replace : item;
    });
};

/**
 * var str = "This {name} is test. {age}";
 * str = str.formatFromJson({"name": "ok", "age" : 27});
 * @param json {JSON}
 * @returns {String}
 */
String.prototype.formatFromJson = function (json) {
    var str = this;
    var jsonObj = json;
    if (typeof jsonObj === "string") {
        try {
            jsonObj = JSON.parse(jsonObj);
        } catch (e) {
            //console.error(e);
            jsonObj = json;
            return str;
        }

    }

    var regex = "";
    if (this.formatFromJsonRegExp) {
        regex = this.formatFromJsonRegExp;
    } else {
        String.prototype.formatFromJsonRegExp = /{(\w+)}/g;
        regex = this.formatFromJsonRegExp;
    }
    try {
        /*$.each(json, function (key, value) {
         //alert(key + " -> " + value);
         str = str.replace(regex, function (match, item) {
         return jsonObj[item] !== undefined ? jsonObj[item] : match;
         });
         });*/

        for (var i = 0; i < json.length; i++) {
            str = str.replace(regex, function (match, item) {
                return jsonObj[item] !== undefined ? jsonObj[item] : match;
            });
        }


    } catch (e) {
        //console.error(e);
    }
    return str !== undefined ? str : this;
};

/**
 *
 * @param length {Number}
 * @returns {String}
 */
String.prototype.zeropadding = function (length) {
    var __this = this;
    var __length = length - __this.length;
    var __str = __this;
    while (__length-- > 0) {
        __str = "0" + __str;
    }
    return __str;
};


/**
 *
 * @param formId {String} form id;
 * @returns {JSON}
 */
function form2json(formId) {
    var form1 = document.getElementById(formId);
    var i = 0;
    var length = form1.length;
    var item = {};
    var json = {};
    for (; i < length; i++) {
        item = form1.elements[i];
        if (item.name) {
            json[item.name] = item.value !== undefined ? item.value : '';
        }
    }
    return json;
}

/**
 *
 var InterfaceA = new Interface('InterfaceA', ['methodA', 'methodB']);
 var A = function(){};
 A.prototype = {
        methodA : function(){...},
        methodB : function(){...}
    }
 var a = new A();
 Interface.ensureImplements(a, [InterfaceA, InterfaceB]);
 * @param name {String} the interface's name;
 * @param methods {String[]} the interface method's names;
 * @constructor
 */
var Interface = function (name, methods) {
    if (arguments.length !== 2) {
        throw new Error("Interface constructor called with " + arguments.length +
            "arguments, but expected exactly 2.");
    }
    this.name = name;
    this.methods = [];
    var i = 0;
    var len = methods.length;
    for (; i < len; i++) {
        if (typeof methods[i] !== 'string') {
            throw new Error("Interface constructor expects method names to be "
                + "passed in as a string.");
        }
        this.methods.push(methods[i]);
    }
    if (Interface._initialized) {
        return;
    }

    /**
     * checkMethods(a, InterfaceA)
     * @param object {Interface} the instance of the Interface;
     * @param interfaceClass {Interface} the Interface;
     */
    Interface.prototype.checkMethods = function (object, interfaceClass) {
        var j = 0;
        var methodsLen = interfaceClass.methods.length;
        for (; j < methodsLen; j++) {
            var method = interfaceClass.methods[j];
            if (!object[method] || typeof object[method] !== 'function') {
                throw new Error("Function Interface.ensureImplements: object "
                    + "does not implement the '" + interfaceClass.name
                    + "' interface. Method '" + method + "' was not found.");
            }
        }
    };

    /**
     * Interface.checkImplements(a, [InterfaceA, InterfaceB]);
     * @param object {Interface} the instance of the Interface;
     * @param interfaces {Interface[]} the interface classes;
     */
    Interface.prototype.checkImplements = function (object, interfaces) {
        if (arguments.length < 2) {
            throw new Error("Function Interface.ensureImplements called with " +
                arguments.length + "arguments, but expected at least 2.");
        }
        var i = 0;
        var len = interfaces.length;
        for (; i < len; i++) {
            interfaces[i].check(object);
        }
    };
    /**
     * checkMethods(a, InterfaceA)
     * @param object {Interface} the instance of the Interface;
     */
    Interface.prototype.check = function (object) {
        var __interface = this;
        if (__interface.constructor !== Interface) {
            throw new Error("Function Interface.ensureImplements expects arguments"
                + " two and above to be instances of Interface.");
        }
        __interface.checkMethods(object, __interface);
    };
    Interface._initialized = true;
};
/**
 * Interface.ensureImplements(a, [InterfaceA, InterfaceB]);
 * @param object {Interface} the instance of the Interface;
 * @param interfaces {Interface[]} the interface classes;
 */
Interface.ensureImplements = function (object, interfaces) {
    iterator(interfaces, function (i, v) {
        v.check(object);
    });
};

/**
 *
 * @param arrays {Array}
 * @param callback {Function}  : callback({Number}, {Object}){}
 */
function iterator(arrays, callback) {
    if (typeof callback !== "function") {
        throw new Error("Function callback in iterator() is not a function.");
    }
    var len = arrays.length;
    var i = 0;
    for (; i < len; i++) {
        callback.call(arrays[i], i, arrays[i]);
    }
}
/**
 *
 * @param callback {Function}  : callback({Number}, {Object}){}
 */
Array.prototype.iterator = function (callback) {
    if (typeof callback !== "function") {
        throw new Error("Function callback in iterator() is not a function.");
    }
    var arrays = this;
    iterator(arrays, callback);
};

Array.prototype.contains = function (obj) {
    var __this = this;
    var __return = false;
    iterator(__this, function (i, v) {
        if (v === obj) {
            return __return = true;
        }
    });
    return __return;
};

Array.prototype.add = function (obj) {
    this.push(obj);
};

Array.prototype.remove = function (obj) {
    var index = this.indexOf(obj);
    if (index >= 0) {
        this.splice(index, 1);
    }
};

var Map = function () {
    this.map = [];
    if (Map.__inited) {
        return this;
    }
    /**
     *
     * @param key {Object}
     * @param value {Object}
     * @returns {Map}
     */
    Map.prototype.put = function (key, value) {
        this.map.push({key: key, value: value});
        return this;
    };
    /**
     *
     * @param key {Object}
     * @returns {number} -1: none;
     */
    Map.prototype.indexOf = function (key) {
        var __map = this.map;
        var __return = -1;
        iterator(__map, function (i, v) {
            if (v.key === key) {
                return __return = i;
            }
        });
        return __return;
    };
    /**
     *
     * @param key {Object}
     * @returns {Map}
     */
    Map.prototype.remove = function (key) {
        var __this = this;
        var index = __this.indexOf(key);
        if (index >= 0) {
            __this.map.splice(index, 1);
        }
        return __this;
    };
    /**
     *
     * @param key {Object}
     * @returns {Object} null: none;
     */
    Map.prototype.get = function (key) {
        var __this = this;
        var index = __this.indexOf(key);
        if (index >= 0) {
            return __this.map[index].value;
        }
        return null;
    };
    /**
     *
     * @returns {Map}
     */
    Map.prototype.clear = function () {
        //this.map.splice(0, this.map.length);
        this.map = [];
        return this;
    };
    Map.prototype.size = function () {
        return this.map.length;
    };
    Map.__inited = true;
};

/*
 * @param format {String} def: "yyyy-MM-dd HH:mm:ss.SSS"
 * @returns {String}
 */
Date.prototype.format = function (format) {
    if (!Date.prototype.formatStr) {
        Date.prototype.formatStr = "yyyy-MM-dd HH:mm:ss.SSS";
    }
    var __date = this;
    var __format = format ? format : Date.prototype.formatStr;
    return (__format + "")
        .replace("yyyy", __date.getFullYear() + "")
        .replace("MM", (__date.getMonth() + 1 + "").zeropadding(2))
        .replace("dd", (__date.getDate() + "").zeropadding(2))
        .replace("HH", (__date.getHours() + "").zeropadding(2))
        .replace("mm", (__date.getMinutes() + "").zeropadding(2))
        .replace("ss", (__date.getSeconds() + "").zeropadding(2))
        .replace("SSS", (__date.getMilliseconds() + "").zeropadding(3))
        ;
};
/**
 *
 * @param format {String} def: "yyyy-MM-dd HH:mm:ss.SSS"
 * @returns {Date}
 */
String.prototype.toDate = function (format) {
    if (!Date.prototype.formatStr) {
        Date.prototype.formatStr = "yyyy-MM-dd HH:mm:ss.SSS";
    }
    var __format = format ? format : Date.prototype.formatStr;
    var __date = new Date();
    var __this = this;
    __date.setFullYear(__this.substr(__format.indexOf("yyyy"), 4));
    __date.setMonth(parseInt(__this.substr(__format.indexOf("MM"), 2)) - 1);
    __date.setDate(__this.substr(__format.indexOf("dd"), 2));
    __date.setHours(__this.substr(__format.indexOf("HH"), 2));
    __date.setMinutes(__this.substr(__format.indexOf("mm"), 2));
    __date.setSeconds(__this.substr(__format.indexOf("ss"), 2));
    __date.setMilliseconds(__this.substr(__format.indexOf("SSS"), 3));
    return __date;
};

var LOGGING = (function () {
    var _instance;

    function constructor() {
        return {
            enable: true,
            log: function (msg) {
                if (this.enable) console.log(new Date() + " --> " + msg);
            },
            setEnable: function (enable) {
                this.enable = enable;
            }
        }
    }

    return {
        getInstance: function () {
            if (!_instance) {
                _instance = new constructor();
            }
            return _instance;
        }

    }
})();
