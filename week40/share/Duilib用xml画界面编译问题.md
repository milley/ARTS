# Duilib用xml画界面编译问题

今天在使用VS2019社区版编译完成Duilib库后，打开帮助文档中的例子，到运行到第三个示例，发现无法编译通过。

先贴上代码如下：

```cpp
#pragma once

#define WIN32_LEAN_AND_MEAN
#define _CRT_SECURE_NO_DEPRECATE

#include <Windows.h>
#include <objbase.h>

#include "..\DuiLib\UIlib.h"

using namespace DuiLib;


#ifdef _DEBUG
	#ifdef _UNICODE
	#pragma comment(lib, "duilib.lib")
	#else
	#pragma comment(lib, "DuiLib_d.lib")
	#endif
#else
	#pragma comment(lib, "DuiLib.lib")
	#ifdef _UNICODE
	#else
	#pragma comment(lib, "DuiLib.lib")
	#endif
#endif

class CFrameWindowWnd : public CWindowWnd, public INotifyUI
{
public:
	CFrameWindowWnd() {}
	LPCTSTR GetWindowClassName() const { return _T("UIMainFrame"); }
	UINT GetClassType() const { return UI_CLASSSTYLE_FRAME | CS_DBLCLKS; }
	void OnFinalMessage(HWND /*hwnd*/) { delete this; }

	void Notify(TNotifyUI& msg)
	{
		if (msg.sType == _T("click"))
		{
			if (msg.pSender->GetName() == _T("closebtn"))
			{
				Close();
			}
		}
	}

	LRESULT HandleMessage(UINT uMsg, WPARAM wParam, LPARAM lParam)
	{
		if (uMsg == WM_CREATE)
		{
			m_pm.Init(m_hWnd);

			CDialogBuilder builder;
			CControlUI* pRoot = builder.Create(_T("res\\Test03.xml"), (UINT)0, NULL, &m_pm);
			ASSERT(pRoot && "Failed to parse XML");
			m_pm.AttachDialog(pRoot);

			m_pm.AddNotifier(this);
			return 0;
		}
		else if (uMsg == WM_DESTROY)
		{
			::PostQuitMessage(0);
		}
		else if (uMsg == WM_NCACTIVATE)
		{
			if (!::IsIconic(m_hWnd))
			{
				return (wParam == 0) ? TRUE : FALSE;
			}
		}
		else if (uMsg == WM_NCCALCSIZE)
		{
			return 0;
		}
		else if (uMsg == WM_NCPAINT)
		{
			return 0;
		}
		LRESULT lRes = 0;
		if (m_pm.MessageHandler(uMsg, wParam, lParam, lRes))
		{
			return lRes;
		}
		return CWindowWnd::HandleMessage(uMsg, wParam, lParam);
	}

public:
	CPaintManagerUI m_pm;
};

int APIENTRY WinMain(HINSTANCE hInstance, HINSTANCE /*hPrevInstance*/,
	LPSTR /*lpCmdLine*/, int nCmdShow)
{
	CPaintManagerUI::SetInstance(hInstance);
	CPaintManagerUI::SetResourcePath(CPaintManagerUI::GetInstancePath());

	CFrameWindowWnd* pFrame = new CFrameWindowWnd();
	if (pFrame == NULL) return 0;
	pFrame->Create(NULL, _T("测试"), UI_WNDSTYLE_FRAME, WS_EX_WINDOWEDGE);
	pFrame->ShowWindow(true);
	CPaintManagerUI::MessageLoop();
	return 0;
}
```

第一次编译提示LNK120: 1个无法解析的外部命令：

```build
1>Test03.cpp
1>F:\cpp_project\duilib\DuiLib\Core\UIManager.h(45,2): warning C4091: “typedef ”: 没有声明变量时忽略“DuiLib::EVENTTYPE_UI”的左侧
1>Test03.obj : error LNK2019: 无法解析的外部符号 __imp___CrtDbgReportW，函数 "public: virtual long __thiscall CFrameWindowWnd::HandleMessage(unsigned int,unsigned int,long)" (?HandleMessage@CFrameWindowWnd@@UAEJIIJ@Z) 中引用了该符号
1>..\bin\Test03_d.exe : fatal error LNK1120: 1 个无法解析的外部命令
```

后来发现有其他项目可以正常编译，因此就针对两个项目的设置进行详细比对，后来发现如下差异(当前是Win32 DEBUG模式)：

- C/C++ => 优化 => 优化：已禁用(/Od)
- C/C++ => 代码生成 => 运行库：多线程调试 DLL (/MDd)
- C/C++ => 预编译头：不使用预编译头
