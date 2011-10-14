package alice.tuprologx.eclipse.views;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.core.PrologQuery;
import alice.tuprologx.eclipse.core.PrologQueryFactory;
import alice.tuprologx.eclipse.core.PrologQueryScope;
import alice.tuprologx.eclipse.util.QueryDialog;


public class QueryList extends ViewPart{
	private TreeViewer tree;
	private Tree list;
	private Action add;
	private Action delete;

	public QueryList(){
		super();
	}
	@Override
	public void createPartControl(Composite parent) {
		tree = new TreeViewer(parent);
		list = tree.getTree();
		createActions();
		createToolbar();
		createMenu();
		createContextMenu();
	}

	public void createMenu()
	{
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();;
		menuManager.add(add);
		menuManager.add(delete);
	}

	public void createToolbar()
	{
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
		toolbarManager.add(add);
		toolbarManager.add(delete);
	}

	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				mgr.add(add);
				mgr.add(delete);
			}
		});

		Menu menu = menuMgr.createContextMenu(tree.getControl());
		tree.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr,tree);
	}

	public void createActions()
	{
		add = new Action("Add query...") {
			public void run() {
				try {
					QueryDialog qd = new QueryDialog(null);
					if (qd.open() == Dialog.OK )
					{
						PrologQueryFactory.getInstance().addQuery(qd.getQuery());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		add.setToolTipText("Adds a new Query");
		add.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_ADD));


		delete = new Action("Delete") {
			public void run() {
				TreeItem[] items = list.getSelection();
				for (int i = 0 ; i < items.length ; i++)
				{
					PrologQuery query = (PrologQuery) items[i].getData();
					PrologQueryFactory.getInstance().removeQuery(query);
				}
			}
		};

		delete.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
	}

	@Override
	public void setFocus() {
		list.setFocus();
	}
	
	public void update() {
		PrologQueryFactory factory = PrologQueryFactory.getInstance();
		list.removeAll();
		Vector<PrologQuery> queries = factory.getQueries();
		for( int i = 0; i < queries.size(); i++ )
		{
			PrologQuery query = queries.get(i);
			TreeItem queryNode = new TreeItem(list, 0);
			queryNode.setText(query.getQuery());
			queryNode.setData(query);
			queryNode.setImage(TuProlog.getIconFromResources("queryIcon.gif"));
			list.setFocus();
			list.setSelection(queryNode);
		}
	}
	
	public void addSelectionListener(SelectionListener listener)
	{
		list.addSelectionListener(listener);
	}
	
	public void removeSelectionListener(SelectionListener listener)
	{
		list.removeSelectionListener(listener);
	}
	
	public PrologQuery getSelectedQuery()
	{
		return (PrologQuery) list.getSelection()[0].getData();
	}
}

