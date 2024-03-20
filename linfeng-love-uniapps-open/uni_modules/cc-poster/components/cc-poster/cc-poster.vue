<template>
	<view style="background: #FFFFFF;">
		<!-- <view v-if="loading"></view> -->
		<canvas style="position: absolute;z-index: -1; top: 50px; left: 50px; opacity: 0;" canvas-id="qrcode"></canvas>
		<canvas v-if="!showPoster" :canvas-id="CanvasID" :style="{ width: canvasW + 'px', height: canvasH + 'px' }"></canvas>
		<image v-else lazy-load :src="tempFilePath" mode="widthFix" class="is-response"></image>
	</view>
</template>

<script>
	import uQRCode from './uqrcode.js'
	var _this;
	export default {
		name: 'cc-poster',
		props: {
			CanvasID: {
				//CanvasID 等同于 canvas-id
				Type: String,
				default: 'PosterCanvas'
			},
			imgSrc: {
				//展示图
				Type: String,
				default: ''
			},
			Title: {
				//文本内容
				Type: String,
				default: '省钱兄'
			},
			TitleColor: {
				//标题颜色
				Type: String,
				default: '#333333'
			},
			LineType: {
				//标题显示行数 大于两行是否省略	（注超出2行显示会导致画布布局絮乱）
				Type: [String, Boolean],
				default: true
			},
			OriginalColor: {
				//默认颜色（如原价与扫描二维码颜色）
				Type: String,
				default: '#b8b8b8'
			},
			Width: {
				//画布宽度  (高度根据图片比例计算 单位upx)
				Type: String,
				default: 750
			},
			CanvasBg: {
				//canvas画布背景色
				Type: String,
				default: '#ffffff'
			},
			Referrer: {
				//推荐人信息
				Type: String,
				default: ''
			},
			ViewDetails: {
				//描述提示文字
				Type: String,
				default: ''
			}
		},
		data() {
			return {
				loading: false,
				tempFilePath: '',
				showPoster: false,
				canvasW: 0,
				canvasH: 0,
				canvasImgSrc: '',
				ctx: null
			};
		},
		methods: {
			toSave(url) {
				//#ifndef H5
				uni.getImageInfo({
					src: url,
					success: function(image) {
						uni.saveImageToPhotosAlbum({
							filePath: image.path,
							success: function() {
								uni.showToast({
									title: '海报已保存相册',
									icon: 'success',
									duration: 2000
								});
							}
						});
					}
				});
				//#endif
			},
			async OnCanvas() {
				this.loading = true;
				
				// this.$queue.showLoading('海报生成中...');
				_this.ctx = uni.createCanvasContext(_this.CanvasID, this);
				const C_W = uni.upx2px(_this.Width), //canvas宽度
					C_P = uni.upx2px(30), //canvas Paddng 间距
					C_Q = uni.upx2px(150); //二维码或太阳码宽高
				let _strlineW = 0; //文本宽度
				let _imgInfo = await _this.getImageInfo({
					imgSrc: _this.imgSrc
				}); //广告图
				
				let r = [_imgInfo.width, _imgInfo.height];
				let q = [C_Q, C_Q];
				let imgW = C_W - C_P * 2;
				if (r[0] != imgW) {
					r[1] = Math.floor((imgW / r[0]) * r[1]);
					r[0] = imgW;
				}
				_this.canvasW = C_W;
				_this.canvasH = r[1] + q[1] + 70;
				_this.ctx.setFillStyle(_this.CanvasBg); //canvas背景颜色
				_this.ctx.fillRect(0, 0, C_W, _this.canvasH); //canvas画布大小

				//添加图片展示
				_this.ctx.drawImage(_imgInfo.path, C_P, C_P, r[0], r[1]);
				//添加图片展示 end

				//设置文本
				_this.ctx.setFontSize(uni.upx2px(42)); //设置标题字体大小
				_this.ctx.setFillStyle(_this.TitleColor); //设置标题文本颜色
				let _strLastIndex = 0; //每次开始截取的字符串的索引
				let _strHeight = r[1] + C_P * 2 + 10; //绘制字体距离canvas顶部的初始高度
				let _num = 1;
				for (let i = 0; i < _this.Title.length; i++) {
					_strlineW += _this.ctx.measureText(_this.Title[i]).width;
					if (_strlineW > r[0]) {
						if (_num == 2 && _this.LineType) {
							//文字换行数量大于二进行省略号处理
							_this.ctx.fillText(_this.Title.substring(_strLastIndex, i - 8) + '...', C_P, _strHeight);
							_strlineW = 0;
							_strLastIndex = i;
							_num++;
							break;
						} else {
							_this.ctx.fillText(_this.Title.substring(_strLastIndex, i), C_P, _strHeight);
							_strlineW = 0;
							_strHeight += 20;
							_strLastIndex = i;
							_num++;
						}
					} else if (i == _this.Title.length - 1) {
						_this.ctx.fillText(_this.Title.substring(_strLastIndex, i + 1), C_P, _strHeight);
						_strlineW = 0;
					}
				}
				//设置文本 end
				//添加二维码
				_strHeight += uni.upx2px(20);
				_this.setEwm(_this.ctx, r[0] - q[0] + C_P, _strHeight, q[0], q[1])
				//添加二维码 end

				//添加推荐人与描述
				_this.ctx.setFillStyle(_this.TitleColor);
				_this.ctx.setFontSize(uni.upx2px(36));
				_this.ctx.fillText(_this.Referrer, C_P, _strHeight + q[1] / 2);
				_this.ctx.setFillStyle(_this.OriginalColor);
				_this.ctx.setFontSize(uni.upx2px(34));
				_this.ctx.fillText(_this.ViewDetails, C_P, _strHeight + q[1] / 2 + 20);
				//添加推荐人与描述 end
				//延迟后渲染至canvas上
				setTimeout(function() {
					_this.ctx.draw(true, ret => {
						uni.hideLoading();
						_this.getNewImage();
					});
				}, 600);
			},
			setEwm(ctx, x, y, width, height) {
				uQRCode.make({
					canvasId: 'qrcode',
					componentInstance: _this,
					text: "https://www.baidu.com",
					size: 200,
					margin: 10,
					fileType: 'png',
					success: res => {
						ctx.drawImage(res, x, y, width, height);
					}
				})


			},
			async getImageInfo({
				imgSrc
			}) {
				return new Promise((resolve, errs) => {
					uni.getImageInfo({
						src: imgSrc,
						success: function(image) {
							resolve(image);
						},
						fail(err) {
							errs(err);
							
							uni.showToast({
								title: '海报生成失败',
								mask: false,
								duration: 2000,
								icon: "none"
							});
							uni.hideLoading()
						}
					});
				});
			},
			getNewImage() {
				uni.canvasToTempFilePath({
						canvasId: _this.CanvasID,
						quality: 1,
						complete: res => {
							_this.tempFilePath = res.tempFilePath;
							_this.showPoster = true
							_this.$emit('success', res);
							_this.loading = false;
							uni.hideLoading();
						}
					},
					this
				);
			}
		},
		mounted() {
			_this = this;
			this.OnCanvas();
		}
	};
</script>

<style></style>
