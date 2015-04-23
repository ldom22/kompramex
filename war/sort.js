var stores = ["sanborns","walmart","coppel","elektra","sears","bestbuy","liverpool","costco","famsa"];
var originalRows = [];

window.onload = function(){
    for(j=0;j<stores.length;j++){
        document.getElementById("f"+stores[j]).addEventListener('click',ApplyFilter);
    }
    document.getElementById("maxSelect").addEventListener('change',ApplyFilter);
    document.getElementById("minSelect").addEventListener('change',ApplyFilter);
    /*document.getElementById("rRel").addEventListener('change',ApplyFilter);
    document.getElementById("rMay").addEventListener('change',ApplyFilter);
    document.getElementById("rMen").addEventListener('change',ApplyFilter);*/
    for(j=0; j<numOfRows; j++){
        originalRows[j] = document.getElementById("r"+j).innerHTML;
    }
}

function show() {
    document.getElementById('filterOptions').style.display = '';
}

function priceToNum(price){
    s = price.substring(8,price.length);
    s = s.substring(0,s.length-9);
    s = s.replace('$','');
    s = s.replace(',','');
    var f = parseFloat(s);
    return f;
}

function ApplyFilter(){
    //order by relevance
    var order = [];
    for(i=0;i<numOfRows;i++){
        document.getElementById("r"+i).innerHTML = originalRows[i];
        order[i] = i;
    }
    //filter stores
    for(i=0;i<numOfRows;i++){
        for(j=0;j<stores.length;j++){
            if(document.getElementById("f"+stores[j]).checked==false){
                if(document.getElementById("i"+i).src.indexOf(stores[j])!=-1){
                    document.getElementById("r"+i).style.display = 'none';
                }
            } else {
                if(document.getElementById("i"+i).src.indexOf(stores[j])!=-1){
                    document.getElementById("r"+i).style.display = '';
                }
            }
        }
    }
    //get max price
    s = document.getElementById("maxSelect").value;
    s.replace('$','');
    s.replace(',','');
    var maxf = parseFloat(s);
    //get min price
    s = document.getElementById("minSelect").value;
    s.replace('$','');
    s.replace(',','');
    var minf = parseFloat(s);
    //filter prices
    for(i=0;i<numOfRows;i++){
        //get item price
        var f = priceToNum(document.getElementById("p"+i).innerHTML);
        //filter
        if(document.getElementById("r"+i).style.display==''){
            if((f<minf)||(f>maxf)){
                document.getElementById("r"+i).style.display = 'none';
            }
        }
    }
    //recolor rows
    var alternate = true;
    for(i=0;i<numOfRows;i++){
        if(document.getElementById("r"+i).style.display==''){
            if(alternate==true){
                document.getElementById("r"+i).style.backgroundColor = '#B9CCFF';
            } else {
                document.getElementById("r"+i).style.backgroundColor = '#FFFFFF';
            }
            alternate = !alternate;
        }
    }
    /*//order by highest price
    if(document.getElementById("rMay").checked==true){
        for(i=0;i<numOfRows;i++){
            var swapped = false;
            do {
                
            } while(swapped==true);
        }
    //order by lowest price
    } else if(document.getElementById("rMen").checked==true){
        for(i=0;i<numOfRows;i++){
            
        }
    }*/
}
