//购物车控制层
app.controller('cartController',function($scope,cartService){
    //查询购物车列表
    $scope.findCartList=function(){
        cartService.findCartList().success(
            function(response){
                $scope.cartList=response;
            }
        );
    }

    $scope.addGoodsToCartList=function(itemId,num){
        cartService.addGoodsToCartList(itemId,num).success(
            function(response){
                if(response.success){
                    $scope.findCartList();//刷新列表
                }else{
                    alert(response.message);//弹出错误提示
                }
            }
        );
    }

    //查询购物车列表
    $scope.findCartList=function(){
        cartService.findCartList().success(
            function(response){
                $scope.cartList=response;
                $scope.totalValue=cartService.sum($scope.cartList);//求合计数
            }
        );
    }

    //获取当前用户的地址列表
    $scope.findAddressList=function (){
        cartService.findAddressList().success(
            function (response){
                $scope.addressList=response;
                //设置默认地址
                for (let i = 0; i < $scope.addressList.length; i++) {
                    if($scope.addressList[i].isDefault=='1'){
                        $scope.address= $scope.addressList[i];
                        break;
                    }
                }
            }
        )
    }

    //選擇地址
    $scope.selectAddress=function (address){
        $scope.address=address;
    }

    //判断是否是当前选中的地址
    $scope.isSelectedAddress=function (address){
        if(address==$scope.address){
            return true;
        }else {
            return false;
        }
    }


    $scope.order={paymentType:'1'}//订单对象
    $scope.selectPayType=function (type){
        $scope.order.paymentType=type;

    }

    $scope.submitOrder=function (){
        $scope.order.receiverAreaName=$scope.address.address;
        $scope.order.receiverMobile=$scope.address.mobile;
        $scope.order.reciver=$scope.address.contact;
        cartService.submitOrder($scope.order).success(
            function (response){
                if(response.success){
                    if($scope.order.paymentType=='1'){
                        location.href="pay.html"
                    }else {
                        location.href="paysuccess.html"
                    }
                }else {
                    alert(response.message);
                }
            }
        )
    }




});
