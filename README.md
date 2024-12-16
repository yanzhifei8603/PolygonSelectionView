# PolygonSelectionView
This is an Android custom View
Used to generate a polygon (quadrilateral) selection box, users can freely drag over any vertex, transform the position of the vertex, and then return the coordinates of the four vertices of the selection box for the next step in processing, such as using OpenCV to obtain pixels within the selection box range for image processing

这是一个android自定义View
用于产生一个多边形(四边形)选择框,用户可以自由拖过任意一个顶点,变换顶点的位置,然后返回该选择框的四个顶点坐标,用来进行下一步入处理,例如使用opencv获取选择框范围内的像素进行图像处理.


XML:
<com.sjzrbjx.tablayout.tools.PolygonSelectionView
    android:id="@+id/polygonSelectionView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>


Code:
PolygonSelectionView polygonSelectionView =findViewById(R.id.polygonSelectionView);
List<float[]> coordinate= polygonSelectionView.getCoordinate();
float x1,y1;
x1=coordinate.get(0)[0];
y1=coordinate.get(0)[1];
