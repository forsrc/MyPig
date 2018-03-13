define([ './module' ], function(services) {
    'use strict';
    console.log("--> StringUtils...")

    /**
     * var str = "This {0} is test. {1} {-1}"; str = str.formatStr(["func",
     * "ok"]);
     * 
     * @param args
     *            {Array}
     * @returns {string}
     */
    String.prototype.formatStr = function(args) {
        var str = this;
        var regex = "";
        if (this.formatStrRegExp) {
            regex = this.formatStrRegExp;
        } else {
            String.prototype.formatStrRegExp = /{(-?[0-9]+)}/g;
            regex = this.formatStrRegExp;
        }

        var intVal = 0;
        return str.replace(regex, function(match, number) {
            intVal = Number(number);
            if (intVal >= 0 && intVal < args.length) {
                return args[intVal] || match;
            }
            if (intVal < 0 && args.length + intVal >= 0
                    && args.length + intVal < args.length) {
                return args[args.length + intVal] || match;
            }

        });
    };

    function forEach(json, callback) {
        Object.keys(json).forEach(function(key) {
            var value = json[key];
            if (!value) {
                return;
            }
            callback(value);
        });
    }

    return services.factory("StringUtils", function() {
        return {
            forEach : forEach,
            formatStr : String.formatStr
        };
    });
});
