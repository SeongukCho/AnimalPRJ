function ArrayList() {
    this.array = new Array();
}

ArrayList.prototype = {
    add : function(obj) {
        this.array[this.array.length] = obj;
    },
    size : function() {
        return this.array.length;
    },
    get : function(index) {
        return this.array[index];
    },
    addAll : function(obj) {
        if (obj instanceof Array) {
            for (var i = 0; i < obj.length; i++) {
                this.add(obj[i]);
            }
        } else if (obj instanceof ArrayList) {
            for (var i = 0; i < obj.length(); i++) {
                this.add(obj.get(i));
            }
        }
    },
    remove : function(index) {
        if (this.size() > 0 && index > -1 && index < this.size()) {
            switch (index) {
            case 0:
                this.array.shift();
                break;
            case this.size() - 1:
                this.array.pop();
                break;
            default:
                var head = this.array.slice(0, index);
                var tail = this.array.slice(index + 1);
                this.array = head.concat(tail);
                break;
            }
        }
    },
    iterator : function() {
        return new Iterator(this);
    },
    toJSON : function() {
        if (this.size() == 0)
            return "";
        return $.toJSON(this.array);
    },
    contains : function(obj) {
        var result = 0;
        for (var i = 0; i < this.array.length; i++) {
            if (this.array[i] === obj) {
                result = 1;
                break;
            }
        }
        return result;
    },
    clear : function() {
        this.array = new Array();
    }
};


/* HashMap */
function HashMap() {
    this.keys = new ArrayList();
    this.values = new ArrayList();
}

HashMap.prototype = {
    get : function(key) {
        for (var index = 0; index < this.keys.size(); index++) {
            if (key == this.keys.get(index)) {
                return this.values.get(index);
            }
        }
        return null;
    },
    put : function(key, value) {
        if (this.get(key) != null) {
            this.remove(key);
        }
        this.keys.add(key);
        this.values.add(value);
    },
    containsKey : function(key) {
        for (var index = 0; index < this.keys.size(); index++) {
            if (key == this.keys.get(index)) {
                return true;
            }
        }
        return false;
    },
    size : function() {
        return this.keys.size();
    },
    containsValue : function(values) {
        for (var index = 0; index < this.values.size(); index++) {
            if (values == this.values.get(index)) {
                return true;
            }
        }
        return false;
    },
    getKeys : function() {
        return this.keys;
    },
    getValues : function() {
        return this.values;
    },
    keyIterator : function() {
        return new Iterator(this.keys);
    },
    valueIterator : function() {
        return new Iterator(this.values);
    },
    remove : function(key) {
        for (var index = 0; index < this.keys.size(); index++) {
            if (key == this.keys.get(index)) {
                this.keys.remove(index);
                this.values.remove(index);
                break;
            }
        }
    },
    toJSON : function() {
        if (this.size() == 0)
            return "";
        var ret = [];
        for (var mIter = this.keyIterator(); mIter.hasNext();) {
            var key = mIter.next();
            var value = this.get(key);
            var type = typeof (key);

            var name = "";
            if (type == "number")
                name = '"' + key + '"';
            else if (type == "string")
                name = $.quoteString(key);
            else
                continue;

            var val = $.toJSON(value);
            if (typeof (val) != "string") {
                continue;
            }
            ret.push(name + ":" + val);
        }
        return "{" + ret.join(", ") + "}";
    }
};

/* Iterator */
function Iterator(arrayList) {
    this.arrayList = arrayList;
    this.index = 0;
}

Iterator.prototype = {
    hasNext : function() {
        return this.index < this.arrayList.size();
    },
    next : function() {
        return this.arrayList.get(this.index++);
    }
};