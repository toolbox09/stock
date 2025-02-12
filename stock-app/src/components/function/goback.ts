

export function goback( navi : any ) {
  if(navi.canGoBack()){
    navi.goBack();
  }
}